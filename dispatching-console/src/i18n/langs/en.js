import enLocale from 'element-ui/lib/locale/lang/en'

const en = {
    message: {

        'save': 'Save',
        'cancel': 'Cancel',
        'refresh': 'Refresh',
        'query': 'Query',
        'reset': 'Reset',
        'bulkImport': 'Bulk Import',
        'keyword': 'Keyword',
        'run': 'Run',
        'edit': 'Edit',
        'delete': 'Delete',
        'success': 'Success',
        'failed': 'Failed',
        'detail': 'Detail',
        'download': 'Download',
        'stop': 'Stop',
        'back': 'Back',
        'all': 'ALL',
        'more': '| More',

        // 欢迎界面
        'appRegister': 'App Registration',
        'userRegister': 'User Registration',
        'appNameInputPLH': 'Enter The AppName',
        'appName': 'AppName',
        'appPassword': 'AppPassword',
        'register': 'Register',
        'name': 'Name',
        'phone': 'Phone',
        'email': 'Email',
        'webhook': 'Webhook',
        'welcomeTitle': 'Welcome to use PowerJob!',
        'login': 'Login',
        'logout': 'Logout',
        'changeAppInfo': 'Change AppInfo',
        'newPassword': 'New Password',
        'newPassword2': 'Check New Password',
        'stayLogged': 'Keep me logged in',

        'tabHome': 'Home',
        'tabJobManage': 'Job management',
        'tabJobInstance': 'Job instances',
        'tabWorkflowManage': 'Workflow management',
        'tabWfInstance': 'Workflow instances',
        'tabContainerOps': 'Container DevOps',
        'tabTemplate': 'Template generator',
        'tabContainerManager': 'Container Management',

        'omsServerTime': 'Server Time',
        'omsServerTimezone': 'Server Timezone',
        'localBrowserTime': 'Local Time',
        'localBrowserTimezone': 'Local Timezone',
        'githubURL': 'GitHub Repo',
        'docURL': 'Document Address',
        'totalJobNum': 'Total job num',
        'runningInstanceNum': 'Running instance num',
        'recentFailedInstanceNum': 'Recent failed instance num',
        'workerNum': 'Worker node num',
        'workerAddress': 'Worker address',
        'cpuLoad': 'CPU Load',
        'memoryLoad': 'Memory Load',
        'diskLoad': 'Disk Load',
        'lastActiveTime': 'Last Active Time',

        // JobManage
        'jobId': 'Job ID',
        'instanceId': 'Instance ID',
        'jobName': 'Job name',
        'scheduleInfo': 'Schedule info',
        'executeType': 'Execution type',
        'processorType': 'Processor type',
        'status': 'Status',
        'operation': 'Operation',
        'newJob': 'New job',
        'jobDescription': 'Job description',
        'jobParams': 'Job params',
        'timeExpressionType': 'Time expression type',
        'timeExpressionPlaceHolder': 'Cron expression or number of millions for fixed_rate/fixed_delay job',
        'executeConfig': 'Execution config',
        'javaProcessorInfoPLH': 'Classname, eg: tech.powerjob.HelloWordProcessor',
        'containerProcessorInfoPLH': 'ContainerID#classname, eg: 1#tech.powerjob.HelloWordProcessor',
        'shellProcessorInfoPLH': 'Shell script',
        'pythonProcessorInfoPLH': 'Python script',
        'runtimeConfig': 'Runtime config',
        'maxInstanceNum': 'Max instance num',
        'threadConcurrency': 'Thread concurrency',
        'timeout': 'Time limit (ms)',
        'retryConfig': 'Retry config',
        'taskRetryTimes': 'Instance retry times',
        'subTaskRetryTimes': "Task retry times",
        'workerConfig': 'Worker config',
        'minCPU': 'MinAvailableCPUCores',
        'minMemory': 'MinMemory(GB)',
        'minDisk': 'MinDisk(GB)',
        'clusterConfig': 'Cluster config',
        'designatedWorkerAddress': 'Designated worker address',
        'designatedWorkerAddressPLH': 'Empty for all workers; ip:port,ip:port for specific',
        'maxWorkerNum': 'Max worker num',
        'maxWorkerNumPLH': '0 means no limit',
        'alarmConfig': 'Alarm config',
        'alarmSelectorPLH': 'Alarm receiver(s)',
        'standalone': 'Standalone',
        'broadcast': 'Broadcast',
        'map': 'MAP',
        'mapReduce': 'MapReduce',
        'fixRate': 'Fixed rate (ms)',
        'fixDelay': 'Fixed delay (ms)',
        'workflow': 'Workflow',
        'validateTimeExpression': 'Validate',
        'javaContainer': 'External',
        'runHistory': 'History',
        'reRun': 'Retry',
        'builtIn': 'BUILT_IN',
        'External': 'EXTERNAL',

        // JobInstance
        'wfInstanceId': 'WorkflowInstanceId',
        'normalInstance': 'Normal instance',
        'wfInstance': 'Workflow instance',
        'triggerTime': 'Trigger time',
        'finishedTime': 'Finished time',
        'log': 'Log',
        'runningTimes': 'Running times',
        'taskTrackerAddress': 'TaskTracker address',
        'startTime': 'Start time',
        'expectedTriggerTime': 'Expected trigger time',
        'result': 'Result',
        'subTaskInfo': 'Task info',
        'secondlyJobHistory': 'Secondly job history',
        'subInstanceId': 'SubInstanceId',
        'instanceParams': 'InstanceParams',
        'lifeCycle': 'Life cycle',
        'alertThreshold': 'AlertThreshold',
        'statisticWindow': 'StatisticWindow',
        'silenceWindow': 'SilenceWindow',
        'runByParameter': 'Run by parameter',
        'enteringParameter': 'Entering Parameter',

        // workflowManage
        'wfId': 'Workflow ID',
        'wfName': 'Workflow name',
        'newWorkflow': 'New workflow',
        'wfDescription': 'Description',
        'importJob': 'Import job',
        'deleteJob': 'Delete job',
        'newStartPoint': 'New starting point',
        'newEndPoint': 'New ending point',
        'deleteEdge': 'Delete edge',
        'importJobTitle': "Select jobs",
        'wfTimeExpressionPLH': 'Cron expression for CRON or empty for API',
        'import': 'Import',
        'ntfClickNeedDeleteNode': 'Please click on the node you want to delete.',
        'ntfClickStartPoint': 'Please click on the start node',
        'ntfClickTargetPoint': 'Please click on the end node',
        'ntfClickDeleteEdge': 'Please click on the edge you want to remove.',
        'ntfAddStartPointFirst': 'Please add the starting point first!',
        'ntfInvalidEdge': 'Illegal operation (same origin and destination)!',

        // workflowInstance
        'wfTips': 'tips：Click on a node to view details of the job instance',
        'ntfClickNoInstanceNode': 'No instances have been generated, and details cannot be viewed!',
        'wfInitParams': 'InitParams',

        // 容器
        'newContainer': 'New container',
        'containerType': 'Type',
        'containerGitURL': 'Git URL',
        'branchName': 'Branch',
        'username': 'Username',
        'oldPassword': 'Old password',
        'password': 'Password',
        'containerId': 'ID',
        'containerName': 'Name',
        'containerVersion': 'Version',
        'deployTime': 'Deployed time',
        'deploy': 'Deploy',
        'deployedWorkerList': 'Worker list',
        'uploadTips': 'Drag and drop or click on the file to upload it automatically',

        // 任务实例状态
        'waitingDispatch': 'Waiting dispatch',
        'waitingWorkerReceive': 'Waiting receive',
        'running': 'Running',
        'stopped': 'Stopped',
        'canceled': 'Canceled',
        'canceleded': 'Canceled',
        'wfWaiting': 'Waiting',
        'waitingUpstream': 'Waiting upstream',

        // 新增的提示信息
        'noSelect': 'Please select at least one data item',
        'nodeName': 'Node name',
        'nodeParams': 'Node parameter',
        'enable': 'Enable',
        'skipWhenFailed': 'Allow skips when failed',
        'fullScreen': 'Full Screen',
        'zoomIn': 'Zoom In',
        'zoomOut': 'Zoom out',
        'autoFit': 'Auto Fit',
        'markerSuccess': 'Marked Success',
        'restart': 'restart',
        'wfContext': 'Context',
        'yes': 'YES',
        'no': 'NO',
        'copy': 'Copy',
        'condition': 'Condition',
        'workflowChild': 'Subprocesses',
        'importWorkflowTitle': 'importWorkflowTitle'
    },
    ...enLocale
};

export default en