import request from '@/router/axios'

class StateService {
    async getStateList(url) {
        const response = await request({
            url: url,
            method: 'post'
        })
        return response.data.data;
    }

    async getAsnBillState() {
        return await this.getStateList('/api/wms/state/getAsnBillState');
    }

    async getStorageMethod() {
        return await this.getStateList('/api/wms/state/getStorageMethod');
    }

}

export const stateService = new StateService();
