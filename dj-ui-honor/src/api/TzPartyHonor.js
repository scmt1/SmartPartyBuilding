import request from "@/utils/request";

const serviceName = 'TzPartyHonorService'

export const queryTzPartyHonorList = (data) => {
    return request('/queryTzPartyHonorList', {
        serviceName: serviceName,
        methodName: 'queryTzPartyHonorList',
        bizParams: {
            ...data
        }
    })
}

export const addTzPartyHonor = (data) => {
    return request('/addTzPartyHonor', {
        serviceName: serviceName,
        methodName: 'addTzPartyHonor',
        bizParams: {
            ...data
        }
    })
}

export const updateTzPartyHonor = (data) => {
    return request('/updateTzPartyHonor', {
        serviceName: serviceName,
        methodName: 'updateTzPartyHonor',
        bizParams: {
            ...data
        }
    })
}

export const delTzPartyHonor = (id) => {
    return request('/delTzPartyHonor', {
        serviceName: serviceName,
        methodName: 'delTzPartyHonor',
        bizParams: {
            id: id + ''
        }
    })
}

export const getTzPartyHonorById = (id) => {
    return request('/getTzPartyHonorById', {
        serviceName: serviceName,
        methodName: 'getTzPartyHonorById',
        bizParams: {
            id: id + ''
        }
    })
}
