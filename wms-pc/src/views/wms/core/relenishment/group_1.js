import { dict } from "@/constant/dict";
import { getRelBillNo,getDefaultUser } from "@/api/wms/core/relenishment";
export const group = {
    label: '常规',
    column: [
        {
            prop: "relBillNo",
            label: "补货单编码",
            disabled: true,
            span: 12,
            default: function (data, type) {
                if (type === 'new') {
                    getRelBillNo().then(res => {
                        data.relBillNo = res.data.data;
                    });
                }
            }
        },
        {
            prop: "createTime",
            label: "生成时间",
            disabled: true,
            type: "datetime",
            span: 12,
            default:function(){
                return new Date();
            }
        },
        {
            prop: "createUser",
            label: "创建用户",
            disabled: true,
            span: 12,
            default:function(data, type){
                if (type === 'new') {
                    getDefaultUser().then(res => {
                        data.createUser = res.data.data.userName;
                    });
                }
            }
        },
        {
            prop: "relBillState",
            label: "执行状态",
            type: "select",
            span: 12,
            disabled: true,
            dicUrl: "/api/blade-system/dict/dictionary?code=" + dict.relBillState,
            props: {
                label: "dictValue",
                value: "dictKey"
            },
            default:function(){
                return 10;
            }
        },
        {
            prop: "relBillRemark",
            label: "备注",
            span: 24,
            maxlength: 200,
            type: "textarea"
        }
    ]
}
