import { getStore } from '@/util/store';

export function getParamValue(key) {
    const paramList = getStore({ name: 'param' }) || [];
    const param = paramList.find(u => u.paramKey === key);

    return param ? param.paramValue : '';
}

