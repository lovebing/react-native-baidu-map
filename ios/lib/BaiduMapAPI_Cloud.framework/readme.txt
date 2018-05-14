LBS云检索：包括LBS云检索（周边、区域、城市内、详情）；




--------------------------------------------------------------------------------------

iOS 地图 SDK v3.4.4是适用于iOS系统移动设备的矢量地图开发包

--------------------------------------------------------------------------------------

地图SDK功能介绍（全功能开发包）：

地图：提供地图展示和地图操作功能；

POI检索：支持周边检索、区域检索和城市内兴趣点检索；

地理编码：提供经纬度和地址信息相互转化的功能接口；

线路规划：支持公交、驾车、步行三种方式的线路规划；

覆盖物图层：支持在地图上添加覆盖物（标注、几何图形、热力图、地形图图层等），展示更丰富的LBS信息；

定位：获取当前位置信息，并在地图上展示（支持普通、跟随、罗盘三种模式）；

离线地图：使用离线地图可节省用户流量，提供更好的地图展示效果；

调启百度地图：利用SDK接口，直接在本地打开百度地图客户端或WebApp，实现地图功能；

周边雷达：利用周边雷达功能，开发者可在App内低成本、快速实现查找周边使用相同App的用户位置的功能；

LBS云检索：支持查询存储在LBS云内的自有数据；

特色功能：提供短串分享、Place详情检索、热力图等特色功能，帮助开发者搭建功能更加强大的应用；


--------------------------------------------------------------------------------------
 
 
 【 新 版 提 示 】
 【 注 意 】
 1、自v3.2.0起，百度地图iOS SDK全面支持HTTPS，需要广大开发者导入第三方openssl静态库：libssl.a和libcrypto.a（存放于thirdlib目录下）
 添加方法：在 TARGETS->Build Phases-> Link Binary With Libaries中点击“+”按钮，在弹出的窗口中点击“Add Other”按钮，选择libssl.a和libcrypto.a添加到工程中 。
 
 2、支持CocoaPods导入
 pod setup //更新CocoPods的本地库
 pod search BaiduMapKit  //查看最新地图SDK
 
【新增】
 1.新增 BMKConvertToBaiduMercatorFromBD09LL 与 BMKConvertToBD09LLFromBaiduMercator 方法，用于百度经纬度和百度墨卡托之间的转换。
 2.新增 CLLocationCoordinate2D BMKCoordTrans(CLLocationCoordinate2D coordinate, BMK_COORD_TYPE fromType, BMK_COORD_TYPE toType); 方法，支持WGS84LL->BD09LL, GCJ02LL->BD09LL, BD09LL->GCJ02LL三种经纬度之间的直接转换。

【修复】
 1.支持iOS11系统定位权限
 2.个性化地图部分catlog不显示的问题
 3.室内图无背景色的问题
 4.polygon绘制大量节点折线，超出数量，产生飞线问题
 5.部分场景下，点击离线地图crash的问题
