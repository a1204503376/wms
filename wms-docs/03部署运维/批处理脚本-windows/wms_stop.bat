set port=5101
for /f "tokens=1-5" %%i in ('netstat -ano ^| findstr ":%port%"') do (taskkill /f /pid %%m)

pause