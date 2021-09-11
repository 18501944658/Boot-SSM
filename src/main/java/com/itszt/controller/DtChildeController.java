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
public class DtChildeController {

    final static Map<String, String> socreentMap = new HashMap<>();

    static {
        socreentMap.put("庞大汽贸集团股份有限公司", "74.73");
        socreentMap.put("辽宁方大集团实业有限公司", "55.16");
        socreentMap.put("浙商中拓集团股份有限公司", "54.53");
        socreentMap.put("海航集团有限公司", "64.27");
        socreentMap.put("华夏幸福基业控股股份公司", "54.6");
        socreentMap.put("北大方正集团有限公司", "66.55");
    }

    @Autowired
    private PathDetailDao pathDetailDao;


    @Autowired
    private SubPathDao subPathDao;


    @Autowired
    private InSightDao inSightDao;

    @Autowired
    private MembersDao membersDao;

    private static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 10, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

    @GetMapping
    @RequestMapping("/selectone")
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
                    return subPathDao.selectByFromName(entNameList.get(one).trim());
                }
            };
            paramList.add(callable);
        }
        for (int i = 0; i < entNameList.size(); i++) {
            Integer one = i;
            Callable<List<DtResultDto>> callable = new Callable<List<DtResultDto>>() {
                @Override
                public List<DtResultDto> call() throws Exception {
                    return subPathDao.selectByToName(entNameList.get(one).trim());
                }
            };
            paramList.add(callable);
        }
        /**根据指定企业路径查询路径详情**/
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
        }).filter(r ->
                /**过滤掉源事件的**/
                !entNameList.contains(r.getSrcName())
        )
                .filter(Objects::nonNull).collect(Collectors.toList());

        Set<String> getSrcId = result.parallelStream().map(DtResultDto::getSrcId).distinct().collect(Collectors.toSet());
        Set<String> getSearchName = result.parallelStream().map(DtResultDto::getSearchName).distinct().collect(Collectors.toSet());
        Set<String> getFromId = result.parallelStream().map(DtResultDto::getFromId).distinct().collect(Collectors.toSet());
        Set<String> getToId = result.parallelStream().map(DtResultDto::getToId).distinct().collect(Collectors.toSet());
        Set<String> entidlists = new HashSet<>();
        entidlists.addAll(getSrcId);
        entidlists.addAll(getFromId);
        entidlists.addAll(getToId);
        List<Insight> insights = inSightDao.selectInEntid(entidlists.stream().collect(Collectors.toList()));
        List<Insight> insightsSearch = inSightDao.selectInEntid(getSearchName.stream().collect(Collectors.toList()));
        insights.addAll(insightsSearch);
        result.stream().forEach(r -> {
            Insight searchId = insights.parallelStream().filter(m -> m.getEntname().equals(r.getSearchName())).findFirst().orElse(null);
            Insight fromId = insights.parallelStream().filter(m -> m.getEntid().equals(r.getFromId())).findFirst().orElse(null);
            Insight toId = insights.parallelStream().filter(m -> m.getEntid().equals(r.getToId())).findFirst().orElse(null);
            if (searchId != null)
                r.setSrcIdScore(searchId.getTotalValue().toString());
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
                .distinct()
                .filter(n -> StringUtils.isNotEmpty(n.getToGroupId()) && StringUtils.isNotEmpty(n.getSrcGroupId()))
                .filter(n -> n.getSrcGroupId().equals(n.getToGroupId())).collect(Collectors.toList());
        List<String> pathIdList = poiList.parallelStream().map(DtResultDto::getPathId).distinct().collect(Collectors.toList());
        List<PathDetail> pathDetails = pathDetailDao.selectPathAllByPathId(pathIdList);
        getAllPath(pathDetails, poiList);
        List<DtResultDto> allpoiList = poiList.parallelStream()
                .filter(r -> r.getPath().contains("投资")).filter(r -> !r.getPath().contains("任职"))
                .collect(Collectors.toList())
                .stream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getPath() + ";"+o.getSearchName()+";" + o.getSrcName() + ";" + o.getSrcIdScore() + ";" + o.getToName() + ";" + o.getToIdScore()))), ArrayList::new));

        ExcelUtils.writeExcel(request, response, allpoiList, DtResultDto.class, "传导结果",Collections.EMPTY_LIST);
    }

    public List<DtResultDto> getExportDate(String srcName) throws InterruptedException {
        if (StringUtils.isEmpty(srcName)) {
            return Collections.EMPTY_LIST;
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
                    return subPathDao.selectByFromName(entNameList.get(one).trim());
                }
            };
            paramList.add(callable);
        }
        for (int i = 0; i < entNameList.size(); i++) {
            Integer one = i;
            Callable<List<DtResultDto>> callable = new Callable<List<DtResultDto>>() {
                @Override
                public List<DtResultDto> call() throws Exception {
                    return subPathDao.selectByToName(entNameList.get(one).trim());
                }
            };
            paramList.add(callable);
        }
        /**根据指定企业路径查询路径详情**/
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
        }).filter(r ->
                /**过滤掉源事件的**/
                !entNameList.contains(r.getSrcName())
        )
                .filter(Objects::nonNull).collect(Collectors.toList());

        Set<String> getSrcId = result.parallelStream().map(DtResultDto::getSrcId).distinct().collect(Collectors.toSet());
        Set<String> getSearchName = result.parallelStream().map(DtResultDto::getSearchName).distinct().collect(Collectors.toSet());
        Set<String> getFromId = result.parallelStream().map(DtResultDto::getFromId).distinct().collect(Collectors.toSet());
        Set<String> getToId = result.parallelStream().map(DtResultDto::getToId).distinct().collect(Collectors.toSet());
        Set<String> entidlists = new HashSet<>();
        entidlists.addAll(getSrcId);
        entidlists.addAll(getFromId);
        entidlists.addAll(getToId);
        List<Insight> insights = inSightDao.selectInEntid(entidlists.stream().collect(Collectors.toList()));
        List<Insight> insightsSearch = inSightDao.selectInEntid(getSearchName.stream().collect(Collectors.toList()));
        insights.addAll(insightsSearch);
        result.stream().forEach(r -> {
            Insight searchId = insights.parallelStream().filter(m -> m.getEntname().equals(r.getSearchName())).findFirst().orElse(null);
            Insight fromId = insights.parallelStream().filter(m -> m.getEntid().equals(r.getFromId())).findFirst().orElse(null);
            Insight toId = insights.parallelStream().filter(m -> m.getEntid().equals(r.getToId())).findFirst().orElse(null);
//            if (searchId != null)
//                r.setSrcIdScore(searchId.getTotalValue().toString());
//            if (fromId != null)
//                r.setFromIdScore(fromId.getTotalValue().toString());
//            if (toId != null)
//                r.setToIdScore(toId.getTotalValue().toString());
            if (searchId != null){
                if (socreentMap.containsKey(r.getSearchName())) {
                    r.setSrcIdScore(socreentMap.get(r.getSearchName()));
                }else {
                    r.setSrcIdScore(searchId.getTotalValue().toString());
                }
            }
            if (fromId != null){
                if (socreentMap.containsKey(r.getFromName())) {
                    r.setSrcIdScore(socreentMap.get(r.getFromName()));
                }else {
                    r.setFromIdScore(fromId.getTotalValue().toString());
                }
            }
            if (toId != null){
                if (socreentMap.containsKey(r.getToName())) {
                    r.setSrcIdScore(socreentMap.get(r.getToName()));
                }else {
                    r.setToIdScore(toId.getTotalValue().toString());
                }
            }
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
                .distinct()
                .filter(n -> StringUtils.isNotEmpty(n.getToGroupId()) && StringUtils.isNotEmpty(n.getSrcGroupId()))
                .filter(n -> n.getSrcGroupId().equals(n.getToGroupId())).collect(Collectors.toList());
        List<String> pathIdList = poiList.parallelStream().map(DtResultDto::getPathId).distinct().collect(Collectors.toList());
        List<PathDetail> pathDetails = pathDetailDao.selectPathAllByPathId(pathIdList);
        getAllPath(pathDetails, poiList);
        List<DtResultDto> allpoiList = poiList.parallelStream()
                .filter(r -> r.getPath().contains("投资")).filter(r -> !r.getPath().contains("任职"))
                .collect(Collectors.toList())
                .stream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getPath() + ";"+o.getSearchName()+";" + o.getSrcName() + ";" + o.getSrcIdScore() + ";" + o.getToName() + ";" + o.getToIdScore()))), ArrayList::new));

        return allpoiList;
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
