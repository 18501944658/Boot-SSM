package com.itszt.controller;

import com.itszt.domain.DtResultDto;
import com.itszt.domain.Insight;
import com.itszt.domain.Members;
import com.itszt.domain.PathDetail;
import com.itszt.repositry.InSightDao;
import com.itszt.repositry.MembersDao;
import com.itszt.repositry.PathDetailDao;
import com.itszt.repositry.SubPathDao;
import com.itszt.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/dt")
public class DtController {


    @Autowired
    private PathDetailDao pathDetailDao;


    @Autowired
    private SubPathDao subPathDao;


    @Autowired
    private InSightDao inSightDao;

    @Autowired
    private MembersDao membersDao;

    @Autowired
    private DtChildeController dtChildeController;

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

    @GetMapping
    @RequestMapping("/select")
    void select(@RequestParam("srcName") String srcName, HttpServletResponse response, HttpServletRequest request) throws IOException, InterruptedException {
        if (StringUtils.isEmpty(srcName)) {
            return;
        }

        List<Callable<List<DtResultDto>>> paramList = new ArrayList<>();
        List<String> entNameList = Arrays.asList(srcName.split(","));
        /***获取所有成员的集团**/
        List<Members> members = membersDao.selectAllMembers();
        for (int i = 0; i < entNameList.size(); i++) {
            Integer one = i;
            Callable<List<DtResultDto>> callable = new Callable<List<DtResultDto>>() {
                @Override
                public List<DtResultDto> call() throws Exception {
                    return subPathDao.selectBySrcName(entNameList.get(one).trim());
                }
            };
            paramList.add(callable);
        }
        /**根据指定企业路径查询路径详情,获取了所有的非源事件**/
        List<Future<List<DtResultDto>>> futures = threadPoolExecutor.invokeAll(paramList);
        List<DtResultDto> result = futures.stream().flatMap(r -> {
            try {
                return r.get().stream();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        Set<String> getSrcId = result.parallelStream().map(DtResultDto::getSrcId).distinct().collect(Collectors.toSet());
        Set<String> getFromId = result.parallelStream().map(DtResultDto::getFromId).distinct().collect(Collectors.toSet());
        Set<String> getToId = result.parallelStream().map(DtResultDto::getToId).distinct().collect(Collectors.toSet());
        Set<String> entidlists = new HashSet<>();
        entidlists.addAll(getSrcId);
        entidlists.addAll(getFromId);
        entidlists.addAll(getToId);
        if (result.isEmpty()) {
            List<DtResultDto> exportDate = dtChildeController.getExportDate(srcName);
            List<DtResultDto> allpoiList = exportDate.parallelStream()
                    .filter(r -> r.getPath().contains("投资")).filter(r -> !r.getPath().contains("任职"))
                    .filter(r -> {
                        String path = r.getPath();
                        String lastEntName = path.substring(path.lastIndexOf(">") + 1);
                        String srcBeginName = path.substring(0, path.indexOf("-"));
                        if (lastEntName.equals(r.getToName()) && srcBeginName.equals(r.getSrcName()))
                            return true;
                        return false;
                    })
                    .collect(Collectors.toList())
                    .stream().collect(
                            Collectors.collectingAndThen(
                                    Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getPath() + ";" + o.getSearchName() + ";" + o.getSrcName() + ";" + o.getSrcIdScore() + ";" + o.getToName() + ";" + o.getToIdScore()))), ArrayList::new));

            ExcelUtils.writeExcel(request, response, allpoiList, DtResultDto.class, "传导结果");
            return;
        }
        List<Insight> insights = inSightDao.selectInEntid(entidlists.stream().collect(Collectors.toList()));
        result.stream().forEach(r -> {
            Insight srcId = insights.parallelStream().filter(m -> m.getEntid().equals(r.getSrcId())).findFirst().orElse(null);
            Insight fromId = insights.parallelStream().filter(m -> m.getEntid().equals(r.getFromId())).findFirst().orElse(null);
            Insight toId = insights.parallelStream().filter(m -> m.getEntid().equals(r.getToId())).findFirst().orElse(null);
            if (srcId != null)
                r.setSrcIdScore(srcId.getTotalValue().toString());
            if (fromId != null)
                r.setFromIdScore(fromId.getTotalValue().toString());
            if (toId != null)
                r.setToIdScore(toId.getTotalValue().toString());
            Members membersSrcId = members.parallelStream().filter(m -> m.getMemberName().equals(r.getSrcName())).findFirst().orElse(null);
            Members membersFromId = members.parallelStream().filter(m -> m.getMemberName().equals(r.getFromName())).findFirst().orElse(null);
            Members membersToId = members.parallelStream().filter(m -> m.getMemberName().equals(r.getToName())).findFirst().orElse(null);
            if (membersSrcId != null) {
                r.setSrcGroupName(membersSrcId.getGroupName());
                r.setSrcGroupId(membersSrcId.getGroupId());
            }
            if (membersFromId != null) {
                r.setFromGroupName(membersFromId.getGroupName());
                r.setFromGroupId(membersFromId.getGroupId());
            }
            if (membersToId != null) {
                r.setToGroupName(membersToId.getGroupName());
                r.setToGroupId(membersToId.getGroupId());
            }
        });

        /**去除和源企业集团不同的数据**/
        List<DtResultDto> poiList = result.parallelStream()
                .filter(n -> StringUtils.isNotEmpty(n.getToGroupId()) && StringUtils.isNotEmpty(n.getSrcGroupId()))
                .filter(n -> n.getSrcGroupId().equals(n.getToGroupId())).collect(Collectors.toList());
        List<String> pathIdList = poiList.parallelStream().map(DtResultDto::getPathId).distinct().collect(Collectors.toList());
        List<PathDetail> pathDetails = pathDetailDao.selectPathAllByPathId(pathIdList);
        getAllPath(pathDetails, poiList);
        List<DtResultDto> exportDate = dtChildeController.getExportDate(srcName);

        List<DtResultDto> allpoiList = poiList.parallelStream()
                .filter(r -> r.getPath().contains("投资")).filter(r -> !r.getPath().contains("任职"))
                .filter(r -> {
                    String path = r.getPath();
                    String lastEntName = path.substring(path.lastIndexOf(">") + 1);
                    String srcBeginName = path.substring(0, path.indexOf("-"));
                    if (lastEntName.equals(r.getToName()) && srcBeginName.equals(r.getSrcName()))
                        return true;
                    return false;
                })
                .collect(Collectors.toList())
                .stream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getPath() + ";" + o.getSearchName() + ";" + o.getSrcName() + ";" + o.getSrcIdScore() + ";" + o.getToName() + ";" + o.getToIdScore()))), ArrayList::new));
        allpoiList.addAll(exportDate);
        List<DtResultDto> resultDtos = allpoiList.parallelStream()
                .filter(r -> r.getPath().contains("投资")).filter(r -> !r.getPath().contains("任职"))
                .collect(Collectors.toList())
                .stream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getPath() + ";" + o.getSearchName() + ";" + o.getSrcName() + ";" + o.getSrcIdScore() + ";" + o.getToName() + ";" + o.getToIdScore()))), ArrayList::new))
                .parallelStream()
                .sorted(Comparator.comparing(DtResultDto::getSearchName).thenComparing(Comparator.comparing(DtResultDto::getPath)))
                .collect(Collectors.toList());

        /***受影响企业  分值等都一样  path留下最短的那个**/
        Map<String, List<DtResultDto>> mapresult = resultDtos.parallelStream().collect(Collectors.groupingBy(o -> o.getSearchName() + ";" + o.getSrcName() + ";" + o.getSrcIdScore() + ";" + o.getToName() + ";" + o.getToIdScore()));
        List<DtResultDto> poi =new ArrayList<>();
        mapresult.forEach((x, y) -> {
            DtResultDto dtResultDto = y.parallelStream().sorted((m, n) -> {
                        if (m.getPath().length() - n.getPath().length() > 0) {
                            return 1;
                        } else if (m.getPath().length() - n.getPath().length() < 0) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
            ).collect(Collectors.toList()).get(0);
            poi.add(dtResultDto);
        });
        ExcelUtils.writeExcel(request, response, poi, DtResultDto.class, "传导结果");
    }

    /***
     * 获完成路径
     * @param pathDetails
     * @param poiList
     * @return
     */
    public void getAllPath(List<PathDetail> pathDetails, List<DtResultDto> poiList) {
        poiList.stream().forEach(r -> {
            StringBuilder path = new StringBuilder();
//            String path = new String();
            List<PathDetail> allPaths = pathDetails.parallelStream().filter(m -> m.getPathId().equals(r.getPathId())).collect(Collectors.toList());
            log.info("当前拼接path,主体Src企业为:{},pathDetail 一共{}条", r.getSrcName(), allPaths.size());
            if (allPaths.isEmpty())
                return;
            /***
             * 目标源企业
             */
            List<PathDetail> fromSrc = allPaths.parallelStream().filter(m -> m.getFromId().equals(r.getSrcId()) && !m.getToId().equals(r.getSrcId())).collect(Collectors.toList());
            if (fromSrc.isEmpty())
                return;
            /**获取起点的节点,判断起点节点toid是否为当前遍历的r数据中的toList,是则拼接path结束,不是则将当前起点节点的to节点作为from节点,当前遍历r的to节点作为终点节点循环查**/
            PathDetail pathDetail = fromSrc.get(0);
            path.append(r.getSrcName());

            if (pathDetail.getToId().equals(r.getToId()) && pathDetail.getFromId().equals(r.getSrcId())) {
                log.info("当前拼接path,主体Src企业为:{},pathDetail 就一条,r中From为:{},To为:{},===========pathDetail中的From为:{},To为:{};", r.getSrcName(), r.getFromName(), r.getToName(), pathDetail.getFromName(), pathDetail.getToName());
                path.append("-" + pathDetail.getRelationtype() + "->" + pathDetail.getToName());
                r.setPath(path.toString());
                log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            } else {
                getToDetail(allPaths, pathDetail.getToId(), r.getToId(), path, pathDetail);
                r.setPath(path.toString());
                log.info("当前为多路径拼接path：{}================================================================", path.toString());
            }
            log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        });

        System.out.println("pathDetails = " + pathDetails);

    }


    /**
     * 获取下一个节点
     *
     * @param pathDetails
     * @return
     */
    public void getToDetail(List<PathDetail> pathDetails, String fromId, String toEndId, StringBuilder path, PathDetail pathDetailnew) {
        path.append("-" + pathDetailnew.getRelationtype() + "->" + pathDetailnew.getToName());
        List<PathDetail> collect = pathDetails.parallelStream().filter(r -> r.getFromId().equals(fromId) && !r.getToId().equals(fromId)).collect(Collectors.toList());
        if (collect.isEmpty())
            return;
        PathDetail pathDetail = collect.get(0);
        if (pathDetail.getToName().equals(toEndId)) {
            path.append("-" + pathDetail.getRelationtype() + "->" + pathDetail.getToName());
        } else {
            getToDetail(pathDetails, pathDetail.getToId(), toEndId, path, pathDetail);
        }
    }


    public void getAllPathNew(List<PathDetail> pathDetails, List<DtResultDto> poiList) {
        poiList.stream().forEach(r -> {
            StringBuilder path = new StringBuilder();
//            String path = new String();
            List<PathDetail> allPaths = pathDetails.parallelStream().filter(m -> m.getPathId().equals(r.getPathId())).collect(Collectors.toList());
            log.info("当前拼接path,主体Src企业为:{},pathDetail 一共{}条", r.getSrcName(), allPaths.size());
            if (allPaths.isEmpty())
                return;
            /***
             * 目标源企业
             */
            List<PathDetail> fromSrc = allPaths.parallelStream().filter(m -> m.getFromId().equals(r.getSrcId()) && !m.getToId().equals(r.getSrcId())).collect(Collectors.toList());
            if (fromSrc.isEmpty())
                return;
            /**获取起点的节点,判断起点节点toid是否为当前遍历的r数据中的toList,是则拼接path结束,不是则将当前起点节点的to节点作为from节点,当前遍历r的to节点作为终点节点循环查**/
            PathDetail pathDetail = fromSrc.get(0);
            path.append(r.getSrcName());
            getToDetailNew(allPaths, pathDetail.getToId(), path, pathDetail);
            r.setPath(path.toString());
            log.info("当前为多路径拼接path：{}================================================================", path.toString());
            log.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        });

    }

    public void getToDetailNew(List<PathDetail> pathDetails, String fromId, StringBuilder path, PathDetail pathDetailnew) {
        path.append("-" + pathDetailnew.getRelationtype() + "->" + pathDetailnew.getToName());
        List<PathDetail> collect = pathDetails.parallelStream().filter(r -> r.getFromId().equals(fromId) && !r.getToId().equals(fromId)).collect(Collectors.toList());
        if (collect.isEmpty())
            return;
        PathDetail pathDetail = collect.get(0);
        getToDetailNew(pathDetails, pathDetail.getToId(), path, pathDetail);
    }


}
