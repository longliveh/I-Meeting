package com.example.auth.demo.service.face.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.*;
import com.arcsoft.face.enums.ImageFormat;
import com.example.auth.demo.entity.common.ImageInfo;
import com.example.auth.demo.entity.face.FaceUserInfo;
import com.example.auth.demo.entity.face.ProcessInfo;
import com.example.auth.demo.entity.face.UserFaceInfo;
import com.example.auth.demo.factory.FaceEngineFactory;
import com.example.auth.demo.mapper.user.UserFaceInfoMapper;
import com.example.auth.demo.service.face.FaceEngineService;
import com.example.auth.demo.service.room.RoomService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.*;

@Service
public class FaceEngineServiceImpl implements FaceEngineService {

    public final static Logger logger = LoggerFactory.getLogger(FaceEngineServiceImpl.class);

    @Value("${config.freesdk.app-id}")
    public String appId;

    @Value("${config.freesdk.sdk-key}")
    public String sdkKey;

    @Value("${config.freesdk.thread-pool-size}")
    public Integer threadPoolSize;

    @Value("${config.cacheExpireAfterWrite}")
    public Integer cacheExpireAfterWrite;

    @Value("${config.passRate}")
    private Integer passRate;

    @Autowired
    private RoomService roomService;



    private ExecutorService executorService;

    private LoadingCache<Integer, List<UserFaceInfo>> faceGroupCache;

    @Autowired
    private UserFaceInfoMapper userFaceInfoMapper;

    private GenericObjectPool<FaceEngine> extractFaceObjectPool;
    private GenericObjectPool<FaceEngine> compareFaceObjectPool;

    @PostConstruct
    public void init() {
        initCache();
        executorService = Executors.newFixedThreadPool(threadPoolSize);
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(threadPoolSize);
        poolConfig.setMaxTotal(threadPoolSize);
        poolConfig.setMinIdle(threadPoolSize);
        poolConfig.setLifo(false);
        //设置支持人脸检测、支持人脸识别、支持年龄检测、支持性别检测
        extractFaceObjectPool = new GenericObjectPool(new FaceEngineFactory(appId, sdkKey, FunctionConfiguration.builder().supportFaceDetect(true).supportFaceRecognition(true).supportAge(true).supportGender(true).build()), poolConfig);//底层库算法对象池
        //设置支持人脸识别
        compareFaceObjectPool = new GenericObjectPool(new FaceEngineFactory(appId, sdkKey, FunctionConfiguration.builder().supportFaceRecognition(true).build()), poolConfig);//底层库算法对象池

    }

    /**
     * 初始化缓存
     */
    private void initCache() {
        this.faceGroupCache = CacheBuilder
                .newBuilder()
                .maximumSize(100)
                .expireAfterWrite(cacheExpireAfterWrite, TimeUnit.MINUTES)
                .build(new CacheLoader<Integer, List<UserFaceInfo>>() {
                    @Override
                    public List<UserFaceInfo> load(Integer room_id) throws Exception {
                        return roomService.getCunnentParticipant(room_id);
                    }
                });
    }

    private int plusHundred(Float value) {
        BigDecimal target = new BigDecimal(value);
        BigDecimal hundred = new BigDecimal(100f);
        return target.multiply(hundred).intValue();

    }


    @Override
    public List<FaceInfo> detectFaces(ImageInfo imageInfo) {
        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = extractFaceObjectPool.borrowObject();

            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();

            //人脸检测
            faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
            return faceInfoList;
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                extractFaceObjectPool.returnObject(faceEngine);
            }

        }

        return null;
    }

    /**
     * 获取图片中人脸的年龄、性别
     * @param imageInfo
     * @return
     */

    @Override
    public List<ProcessInfo> process(ImageInfo imageInfo){
        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = extractFaceObjectPool.borrowObject();
            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
            //人脸检测
            faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);
            int processResult = faceEngine.process(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList, FunctionConfiguration.builder().supportAge(true).supportGender(true).build());
            List<ProcessInfo> processInfoList=Lists.newLinkedList();

            List<GenderInfo> genderInfoList = new ArrayList<GenderInfo>();
            //性别提取
            int genderCode = faceEngine.getGender(genderInfoList);
            //年龄提取
            List<AgeInfo> ageInfoList = new ArrayList<AgeInfo>();
            int ageCode = faceEngine.getAge(ageInfoList);
            for (int i = 0; i <genderInfoList.size() ; i++) {
                ProcessInfo processInfo=new ProcessInfo();
                processInfo.setGender(genderInfoList.get(i).getGender());
                processInfo.setAge(ageInfoList.get(i).getAge());
                processInfoList.add(processInfo);
            }
            return processInfoList;

        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                extractFaceObjectPool.returnObject(faceEngine);
            }
        }

        return null;

    }
    /**
     * 人脸特征
     * @param imageInfo
     * @return
     */
    @Override
    public byte[] extractFaceFeature(ImageInfo imageInfo) throws InterruptedException {

        FaceEngine faceEngine = null;
        try {
            //获取引擎对象
            faceEngine = extractFaceObjectPool.borrowObject();

            //人脸检测得到人脸列表
            List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();

            //人脸检测
            int i = faceEngine.detectFaces(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList);

            if (CollectionUtil.isNotEmpty(faceInfoList)) {
                FaceFeature faceFeature = new FaceFeature();
                //提取人脸特征
                int code = faceEngine.extractFaceFeature(imageInfo.getRgbData(), imageInfo.getWidth(), imageInfo.getHeight(), ImageFormat.CP_PAF_BGR24, faceInfoList.get(0), faceFeature);
                if (code!=0)
                {
                    throw new RuntimeException("提取人脸特征失败！"+code);
                }
                return faceFeature.getFeatureData();
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (faceEngine != null) {
                //释放引擎对象
                extractFaceObjectPool.returnObject(faceEngine);
            }

        }

        return null;
    }

    @Override
    public List<FaceUserInfo> compareFaceFeature(byte[] faceFeature, Integer room_id) throws InterruptedException, ExecutionException {
        List<FaceUserInfo> resultFaceInfoList = Lists.newLinkedList();//识别到的人脸列表

        FaceFeature targetFaceFeature = new FaceFeature();
        targetFaceFeature.setFeatureData(faceFeature);
        List<UserFaceInfo> faceInfoList;
        try {
            faceInfoList = faceGroupCache.get(room_id);//从缓存中提取人脸库
        }catch (Exception e)
        {
            faceInfoList = null;
        }
        if(faceInfoList!=null)
        {
            List<List<UserFaceInfo>> faceUserInfoPartList = Lists.partition(faceInfoList, 10);//分成10一组，多线程处理
            CompletionService<List<FaceUserInfo>> completionService = new ExecutorCompletionService(executorService);
            for (List<UserFaceInfo> part : faceUserInfoPartList) {
                completionService.submit(new CompareFaceTask(part, targetFaceFeature));
            }
            for (int i = 0; i < faceUserInfoPartList.size(); i++) {
                List<FaceUserInfo> faceUserInfoList = completionService.take().get();
                if (CollectionUtil.isNotEmpty(faceInfoList)) {
                    resultFaceInfoList.addAll(faceUserInfoList);
                }
            }

            resultFaceInfoList.sort((h1, h2) -> h2.getSimilarValue().compareTo(h1.getSimilarValue()));//从大到小排序
            return resultFaceInfoList.size()==0?null:resultFaceInfoList;
        }
        return null;
    }


    private class CompareFaceTask implements Callable<List<FaceUserInfo>> {

        private List<UserFaceInfo> faceUserInfoList;
        private FaceFeature targetFaceFeature;


        public CompareFaceTask(List<UserFaceInfo> faceUserInfoList, FaceFeature targetFaceFeature) {
            this.faceUserInfoList = faceUserInfoList;
            this.targetFaceFeature = targetFaceFeature;
        }

        @Override
        public List<FaceUserInfo> call() throws Exception {
            FaceEngine faceEngine = null;
            List<FaceUserInfo> resultFaceInfoList = Lists.newLinkedList();//识别到的人脸列表
            try {
                faceEngine = compareFaceObjectPool.borrowObject();
                for (UserFaceInfo userFaceInfo : faceUserInfoList) {
                    FaceFeature sourceFaceFeature = new FaceFeature();
                    sourceFaceFeature.setFeatureData(userFaceInfo.getFaceFeature().getFeatureData());
                    FaceSimilar faceSimilar = new FaceSimilar();
                    faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature, faceSimilar);
                    Integer similarValue = plusHundred(faceSimilar.getScore());//获取相似值
                    if (similarValue > passRate) {//相似值大于配置预期，加入到识别到人脸的列表

                        FaceUserInfo info = new FaceUserInfo();
                        info.setName(userFaceInfo.getName());
                        info.setUser_id(userFaceInfo.getUserid());
                        info.setSimilarValue(similarValue);
                        resultFaceInfoList.add(info);
                    }
                }
            } catch (Exception e) {
                logger.error("", e);
            } finally {
                if (faceEngine != null) {
                    compareFaceObjectPool.returnObject(faceEngine);
                }
            }

            return resultFaceInfoList;
        }

    }
}
