import { Utils } from './../services/Utils';

export class ProcReslutModel{

        // 消息编码
        MsgCode:string;

        // 消息内容
        MsgContent: string;

        // 关联项目
        MsgItem: string;

        IsNormal:boolean;

        // 判断当前处理是否正常
        IsProcNormal():boolean
        {
            if (Utils.isEmpty(this.MsgCode) || this.MsgCode.startsWith("I"))
            {
                return true;
            }
            return false;
        }
}