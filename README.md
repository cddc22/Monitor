# Monitor [![](https://jitpack.io/v/leavesCZY/Monitor.svg)](https://jitpack.io/#leavesCZY/Monitor)

只需为 OkHttpClient 添加 MonitorInterceptor，就可以自动记录并缓存所有的网络请求信息，并提供可视化页面进行查看

```kotlin
val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(MonitorInterceptor(context = context))
    .build()
```

同时引入 debug 和 release 版本的依赖，release 版本的 MonitorInterceptor 不会做任何操作

```kotlin
dependencyResolutionManagement {
    repositories {
        maven {
            setUrl("https://jitpack.io")
        }
    }
}

dependencies {
    val latestVersion = "x.x.x"
    debugImplementation("com.github.leavesCZY.Monitor:monitor:${latestVersion}")
    releaseImplementation("com.github.leavesCZY.Monitor:monitor-no-op:${latestVersion}")
}
```

![](https://github.com/leavesCZY/Monitor/assets/30774063/8cff9168-8ba2-4bb4-9c38-859128e45f23)