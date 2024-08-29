import request from "../utils/request";

export const getDictByType = (params) => {
    return request('/getByType', {
        serviceName: 'TDictDataService',
        methodName: 'getByType',
        bizParams: {
            type: params
        }
    })
}
