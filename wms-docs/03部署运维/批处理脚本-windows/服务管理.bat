@echo off

rem 每个服务需要手动修改下面的名称
set wms_service_name=wms-nodes

title WMS服务管理 -- %wms_service_name%

color 3e

goto menu

:menu
echo ******************(如果初次需要编辑wms_service_name变量)*******************
echo         * 服务名称：      %wms_service_name%       
echo         * 当前服务状态：  
nssm status %wms_service_name%
echo *****************************************************************************
echo.
echo         1. 启动
echo         2. 停止
echo         3. 卸载服务
echo         4. 安装服务
echo         5. 查看【%wms_service_name%】状态
echo         0. 退出
echo.

goto chose

:chose
echo 请输入选择的项目：

set /p id=

if %id%==1 goto cmd1
if %id%==2 goto cmd2
if %id%==3 goto cmd3
if %id%==4 goto cmd4
if %id%==5 goto cmd5
if %id%==0 goto cmd0

:cmd1
nssm start %wms_service_name%
cls
goto menu

:cmd2
nssm stop %wms_service_name%
cls
goto menu

:cmd3
nssm remove %wms_service_name%
nssm status %wms_service_name%
goto menu

:cmd4
nssm install %wms_service_name%
nssm status %wms_service_name%
goto menu

:cmd5
nssm status %wms_service_name%
goto chose

:cmd0
exit

pause