cli publish --platform APP --type appResource --project wms-pad-uniapp
dir
scp -r  ../unpackage/resources/* ../../WMSApp/app/src/main/assets/apps/

cd ../../WMSApp
gradlew assembleRelease