@echo off

rem ÿ��������Ҫ�ֶ��޸����������
set wms_service_name=wms-nodes

title WMS������� -- %wms_service_name%

color 3e

goto menu

:menu
echo ******************(���������Ҫ�༭wms_service_name����)*******************
echo         * �������ƣ�      %wms_service_name%       
echo         * ��ǰ����״̬��  
nssm status %wms_service_name%
echo *****************************************************************************
echo.
echo         1. ����
echo         2. ֹͣ
echo         3. ж�ط���
echo         4. ��װ����
echo         5. �鿴��%wms_service_name%��״̬
echo         0. �˳�
echo.

goto chose

:chose
echo ������ѡ�����Ŀ��

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