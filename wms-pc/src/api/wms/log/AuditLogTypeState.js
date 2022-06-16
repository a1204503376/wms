import request from '@/router/axios'

class AuditLogTypeStateService {
    async getStateList(url) {
        const response = await request({
            url: url,
            method: 'post'
        })
        return response.data.data;
    }

    async getLpnTypeState() {
        return await this.getStateList('/api/wms/state/getAuditLogTypeState');
    }


}
export const auditLogTypeStateService =new AuditLogTypeStateService();
