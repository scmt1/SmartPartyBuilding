import request from "../utils/request";

export const delPartyMember = (ids) => {
    return request('/deletePartyMemberInfo', {
        serviceName: 'PartyMemberService',
        methodName: 'deletePartyMemberInfo',
        bizParams: {
            ids
        }
    })
}

export const queryPartyMemberList = (data) => {
    return request('/queryPartyMemberList', {
        serviceName: 'PartyMemberService',
        methodName: 'queryPartyMemberList',
        bizParams: {
            ...data

        }
    })
}

export const queryCountActive = (params) => {
    return request('/queryCountActive', {
        serviceName: 'PartyMemberService',
        methodName: 'queryCountActive',
        bizParams: {
            partyMember: params
        }
    })
}

export const partyMemberShift = (partyIds,deptId) => {
    return request('/partyMemberShift', {
        serviceName: 'PartyMemberService',
        methodName: 'partyMemberShift',
        bizParams: {
            partyIds: partyIds,
            deptId: deptId
        }
    })
}

export const resetPassword = (id) => {
    return request('/resetPassword', {
        serviceName: 'PartyMemberService',
        methodName: 'resetPassword',
        bizParams: {
            id: id
        }
    })
}

export const getInfo = (deptId) => {
    return request('/getInfo', {
        serviceName: 'PartyMemberService',
        methodName: 'getInfo',
        bizParams: {
            deptId: deptId
        }
    })
}
export const queryDeletePartyMemberList = (data) => {
    return request('/queryDeletePartyMemberList', {
        serviceName: 'PartyMemberService',
        methodName: 'queryDeletePartyMemberList',
        bizParams: {
            ...data
        }
    })
}

export const recoverDeletePartyMemberInfo = (ids) => {
    return request('/recoverDeletePartyMemberInfo', {
        serviceName: 'PartyMemberService',
        methodName: 'recoverDeletePartyMemberInfo',
        bizParams: {
            ids: ids
        }
    })
}

export const getPartyInfo = (id) => {
    return request('/getPartyInfo', {
        serviceName: 'PartyMemberService',
        methodName: 'getPartyInfo',
        bizParams: {
            id: id
        }
    })
}

export const addPartyMember = (partyMember) => {
    return request('/addPartyMember', {
        serviceName: 'PartyMemberService',
        methodName: 'addPartyMember',
        bizParams: {
            ...partyMember
        }
    })
}
export const uploadImages = (imgUrl) => {
    return request('/uploadImages', {
        serviceName: 'PartyMemberService',
        methodName: 'uploadImages',
        bizParams: {
            imgUrl:imgUrl
        }
    })
}

export const flowOutPartyMemberList = (params) => {
    return request('/flowOutPartyMemberList', {
        serviceName: 'PartyMemberService',
        methodName: 'flowOutPartyMemberList',
        bizParams: {
            ...params
        }
    })
}
export const queryOneselfPartyMemberList = (data) => {
    return request('/queryOneselfPartyMemberList', {
        serviceName: 'PartyMemberService',
        methodName: 'queryOneselfPartyMemberList',
        bizParams: {
            ...data
        }
    })
}

export const queryOneselfFlowPartyMemberList = (data) => {
    return request('/queryOneselfFlowPartyMemberList', {
        serviceName: 'PartyMemberService',
        methodName: 'queryOneselfFlowPartyMemberList',
        bizParams: {
            data
        }
    })
}
export const uploadPartyMemberFile = (data) => {
    return request('/uploadPartyMemberFile', {
        serviceName: 'PartyMemberService',
        methodName: 'uploadPartyMemberFile',
        bizParams: {
            data: data

        }
    })
}






export const setReturnPartyMember = (params) => {
    return request('/setReturnPartyMember', {
        serviceName: 'PartyMemberService',
        methodName: 'setReturnPartyMember',
        bizParams: {
            ids: params
        }
    })
}

export const addFlowOutPartyMember = (params) => {
    return request('/addFlowOutPartyMember', {
        serviceName: 'PartyMemberService',
        methodName: 'addFlowOutPartyMember',
        bizParams: {
            ...params
        }
    })
}
export const downFormwork = () => {
    return request('/downFormwork', {
        serviceName: 'PartyMemberService',
        methodName: 'downFormwork',
        bizParams: {

        }
    })
}


export const queryBoss = (deptId) => {
    return request('/queryBoss', {
        serviceName: 'PartyMemberService',
        methodName: 'queryBoss',
        bizParams: {
            deptId
        }
    })
}


export const getPartMemberListByIdCard = (params) => {
    return request('/getPartMemberListByIdCard', {
        serviceName: 'PartyMemberService',
        methodName: 'getPartMemberListByIdCard',
        bizParams: {
            ...params
        }
    })
}

export const getPartMemberListByDeptId = (id) => {
    return request('/getPartMemberListByDeptId', {
        serviceName: 'PartyMemberService',
        methodName: 'getPartMemberListByDeptId',
        bizParams: {
            deptId: id
        }
    })
}


