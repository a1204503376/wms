cli publish --platform APP --type appResource --project wms-pda-uniapp
dir
scp -r  unpackage/resources/* ../WMSApp/app/src/main/assets/apps/

cd ../WMSApp & gradlew assembleRelease