import {getTree as getSkuTypeTree} from "@/api/wms/basedata/skutype";
import {getList as getSkuLotList} from "@/api/wms/basedata/skulot";

export const group = {
    label: '常规',
    column: [
        {
            prop: "missionLevel",
            label: "优先级",
            type: "inputNumber",
            disabled: false,  //是否置灰
            rules: [{required: true, message: "优先级不能为空", trigger: "blur", pattern: /\S/,}],
        },
        {
            prop: "missionType",
            label: "任务类型",
            type: "select",
            disabled: false,  //是否置灰
            rules: [{required: true, message: "任务类型不能为空", trigger: "blur", pattern: /\S/,}],
            dicUrl: '/api/blade-system/dict/dictionary?code=task_type',
            props: {
                label: 'dictValue',
                value: 'dictKey'
            },
            show:['missionTypeName']
        },
        {
            prop: "pushWay",
            label: "推送方式",
            type: "select",
            // disabled: false,  //是否置灰
            // rules: [{required: true, message: "任务类型不能为空", trigger: "blur", pattern: /\S/,}],
            dicUrl: '/api/blade-system/dict/dictionary?code=task_push_way',
            props: {
                label: 'dictValue',
                value: 'dictKey'
            },
            show:['pushWayName']
        },
        {
            prop: "isSynthesizeTask",
            label: "任务推送是否考虑综合任务",
            type: "radio",
            dicData: [
                {
                    value: 1,
                    label: "是"
                },
                {
                    value: 0,
                    label: "否"
                }
            ],
            default: 0
        },
        {
            prop: "isExceedcountSegment",
            label: "超过单位数量是否分割任务",
            type: "radio",
            dicData: [
                {
                    value: 1,
                    label: "是"
                },
                {
                    value: 0,
                    label: "否"
                }
            ],
            default: 0
        },
        {
            prop: "countWay",
            label: "计算方式",
            type: "select",
            dicUrl: '/api/blade-system/dict/dictionary?code=task_count_way',
            props: {
                label: 'dictValue',
                value: 'dictKey'
            },
            show:['countWayName']
        },
        {
            prop: "missionSynergyFloor",
            label: "协同任务数量下限",
            type: "inputNumber",
            clearable: true,
        },
        {
            prop: "missionAloneUpper",
            label: "独占任务数量上限",
            type: "inputNumber",
            clearable: true,
        },
        {
            prop: "missionSegmentUpper",
            label: "任务分割上限",
            type: "inputNumber",
            clearable: true,
        },
        {
            prop: "remark",
            label: "备注",
            type: "textarea",
            span: 20,
            maxlength: 200
        },
    ]
}
