import { ProcReslutModel } from "./ProcReslutModel";
import { WmsLabModel } from "./DataBase.Model";

export class BarcodeRuleModel {
    barcodeRule: string;
    BarcodeRuleId: number;
    barcodeType: string;
}
export class ProcModelBase {
    // 登录结果
    procResult: ProcReslutModel;
}
export class LoginRltModel extends ProcModelBase {
        // 登录用户信息
        userInfo: LoginUserInfoModel
        /// Token
        tokenKey: string
        // 登录实验室
        labs: WmsLabModel[]
}

export class LoginUserInfoModel {
    user_id:string;//用户ID
    // token
    access_token:string;
    // 登陆用户
    account:string;
    // 替代用户
    avatar:string;
    
    expires_in:number;
    license:string;
    nick_name:string;
    refresh_token:string;
    logintime:string;

    // 角色名称
    role_name:string;
    // 租户ID
    tenant_id:string;
    // token类型
    token_type:string;
    //用户名
    user_name:string;
}
export class ModuleModel {
    DEEP: number;
    FormName: string;
    MenuName: string;
    ModuleID: string;
    ModuleType: number;
    ParentID: string;
}
export class SysUpdateVerModel {
    /// 部门分组id
    OrgGroupId: number;
    /// 系统版本id
    UpdateVerId: number;
    /// 版本号数值
    VerNum: number;
    /// 版本号名称
    VerName: string;
    /// 模块类型
    ModuleType: string;
    /// 更新地址
    UpdateUrl: string;
    /// 更新备注
    UpdateRemark: string;
    /// 版本更新时间
    VerUpdateTime: Date;
    /// 更新类型
    UpdateType: string;
}
export class SysModuleModel {
    IsActive: string;
    ModuleCd: string;
    ModuleId: string;
    ModuleMenuName: string;
    ModuleName: string;
    ModulePath: string;
    ModuleSortOrder: number;
    ModuleType: string;
    PreModuleId: string;
}
export class SysOrgModel {
    ContactsName: string;
    ExternalFlag: string;
    IsActive: string;
    MobilePhone: string;
    OrgCode: string;
    OrgGroupId: number;
    OrgId: number;
    OrgName: string;
    OrgPath: string;
    OrgPreId: number;
    OrgRemark: string;
    ReportTitle: string;
    WhCode: string;
    WhId: number;
}
export class SysUserModel {
    IsActive: string;
    MobilePhone: string;
    OrgCode: string;
    OrgGroupId: number;
    OrgId: number;
    UserCode: string;
    UserId: number;
    UserName: string;
    UserPasswd: string;
    UserRemark: string;
    UserType: string;
}
export class UserPassModel {
    CurrentPwd: string;
    NewPwd: string;
    UserCode: string;
}
export class UsersAndModuleModel {
    Modules: SysModuleModel[];
    Users: SysUserModel[];
}