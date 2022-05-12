import request from '@/router/axios'

class LpnTypeStateService {
    async getStateList(url) {
        const response = await request({
            url: url,
            method: 'post'
        })
        return response.data.data;
    }

    async getLpnTypeState() {
        return await this.getStateList('/api/wms/state/getLpnTypeState');
    }


}
export const lpnTypeStateService =new LpnTypeStateService();
