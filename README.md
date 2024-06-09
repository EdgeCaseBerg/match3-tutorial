# Match 3 Tutorial


## To build the exe:

1. Grab packr https://github.com/libgdx/packr
2. Grab a JRE from https://adoptium.net/download/ for Java 8.
3. Run the following from the root directory

```
java -jar packr-all-4.0.0.jar \
    --platform windows64\
    --jdk OpenJDK8U-jre_x64_windows_hotspot_8u412b08.zip\
    --executable relaxnmatch\
    --classpath desktop/build/libs/desktop-1.0.jar\
    --mainclass space.peetseater.game.DesktopLauncher\
    --resources assets/*\
    --output distribution\
    --minimizejre soft    
    
    java -jar packr-all-4.0.0.jar \
    --platform mac\
    --jdk OpenJDK8U-jre_x64_windows_hotspot_8u412b08.zip\
    --executable relaxnmatch\
    --classpath desktop/build/libs/desktop-1.0.jar\
    --mainclass space.peetseater.game.DesktopLauncher\
    --resources assets/*\
    --output distribution-mac\
    --minimizejre soft 
```

4. Enjoy and relax.