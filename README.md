# Adguard Home WebView App

一个简单的Android应用，启动后全屏打开Adguard Home管理界面。

## 功能特点

- 启动后全屏显示
- 打开本地Adguard Home管理界面 (127.0.0.1:3000)
- 沉浸式导航栏（小白条导航栏沉浸）
- 支持Android 7.0 (API 24) 及以上版本
- 使用GitHub Actions自动构建APK

## 项目结构

```
webview-app/
├── .github/workflows/build.yml    # GitHub Actions工作流
├── app/
│   ├── src/main/
│   │   ├── java/org/adguardhome/
│   │   │   └── MainActivity.java  # 主Activity
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   └── activity_main.xml
│   │   │   ├── mipmap-*/          # 应用图标
│   │   │   └── values/
│   │   │       ├── strings.xml    # 字符串资源
│   │   │       └── styles.xml     # 样式资源
│   │   └── AndroidManifest.xml    # 应用配置
│   └── build.gradle               # 模块构建配置
├── gradle/wrapper/                # Gradle包装器
├── build.gradle                   # 项目构建配置
├── settings.gradle                # 项目设置
└── README.md                      # 项目说明
```

## 快速开始

### 1. 上传到GitHub

#### 方法一：使用脚本（Windows）
```batch
upload-to-github.bat
```

#### 方法二：手动操作
```bash
# 初始化Git仓库
git init

# 添加所有文件
git add .

# 提交代码
git commit -m "Initial commit: Adguard Home WebView app"

# 添加远程仓库（替换为你的仓库地址）
git remote add origin https://github.com/your-username/your-repo.git

# 推送到GitHub
git push -u origin master
```

### 2. 自动构建APK

推送到GitHub后，GitHub Actions会自动构建APK：

1. 访问你的GitHub仓库
2. 点击 "Actions" 标签页
3. 查看构建状态
4. 构建完成后，点击最新的工作流
5. 在 "Artifacts" 部分下载APK

### 3. 安装APK

1. 在Android手机上启用 "允许安装未知来源应用"
2. 下载APK文件
3. 点击安装

## 配置说明

### 修改目标URL

编辑 `app/src/main/java/org/adguardhome/MainActivity.java` 文件：

```java
private static final String TARGET_URL = "http://127.0.0.1:3000";
```

### 应用信息

| 项目 | 值 |
|------|-----|
| 应用名称 | Adguard Home |
| 包名 | org.adguardhome |
| 最低支持版本 | Android 7.0 (API 24) |
| 目标版本 | Android 14 (API 37) |
| 编译版本 | Android 14 (API 37) |

## 技术细节

### 沉浸式导航栏实现

应用使用 `WindowInsetsController` API（Android 11+）实现沉浸式导航栏：

```java
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    getWindow().setDecorFitsSystemWindows(false);
    WindowInsetsController controller = getWindow().getInsetsController();
    if (controller != null) {
        controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        controller.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
    }
}
```

### WebView配置

- 启用JavaScript
- 启用DOM存储
- 支持缩放
- 禁止跳转到外部浏览器

## 常见问题

### Q: 为什么APK无法安装？
A: 请确保在手机设置中启用了"允许安装未知来源应用"。

### Q: 如何修改应用图标？
A: 替换 `app/src/main/res/mipmap-*/` 目录中的图标文件。

### Q: 如何修改应用名称？
A: 编辑 `app/src/main/res/values/strings.xml` 文件中的 `app_name` 字符串。

### Q: GitHub Actions构建失败怎么办？
A: 检查以下几点：
1. 确保仓库是公开的（或启用GitHub Actions）
2. 检查工作流文件语法
3. 查看构建日志中的错误信息

## 许可证

本项目仅用于学习和测试目的。