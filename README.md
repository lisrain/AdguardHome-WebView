# Adguard Home WebView App

一个简单的Android应用，启动后全屏打开Adguard Home管理界面。

## 功能特点

- 启动后全屏显示
- 打开本地Adguard Home管理界面 (127.0.0.1:3000)
- 沉浸式导航栏
- 支持Android 7.0 (API 24) 及以上版本

## 配置

### 修改目标URL

编辑 `app/src/main/java/org/adguardhome/MainActivity.java` 文件中的 `TARGET_URL` 常量：

```java
private static final String TARGET_URL = "http://127.0.0.1:3000";
```

### 应用信息

- 应用名称: Adguard Home
- 包名: org.adguardhome
- 最低支持版本: Android 7.0 (API 24)
- 目标版本: Android 14 (API 37)

## 构建

### 本地构建

确保已安装Android Studio和JDK 17，然后运行：

```bash
./gradlew assembleDebug
```

### GitHub Actions自动构建

推送到GitHub后，GitHub Actions会自动构建APK。

1. Fork或克隆此仓库
2. 推送代码到GitHub
3. 在Actions标签页查看构建状态
4. 构建完成后，在Artifacts中下载APK

## 安装

1. 在手机上启用"允许安装未知来源应用"
2. 下载并安装APK文件
3. 启动应用

## 注意事项

- 应用需要连接到运行Adguard Home的设备
- 默认打开 http://127.0.0.1:3000
- 如果Adguard Home运行在其他地址，请修改 `TARGET_URL`