import request from "../utils/request";

export const checkMeeting = (data) => {
    return request('/checkMeeting', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'checkMeeting',
        bizParams: {
            ...data
        }
    })
}

export const updateTzOrganizationLife = (data) => {
    return request('/updateTzOrganizationLife', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'updateTzOrganizationLife',
        bizParams: {
            ...data
        }
    })
}

export const getTzOrganizationLife = (deptId,id) => {
    return request('/getTzOrganizationLife', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'getTzOrganizationLife',
        bizParams: {
            deptId: deptId,
            id: id,

        }
    })
}

export const endMeeting = (data) => {
    return request('/endMeeting', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'endMeeting',
        bizParams: {
            ...data
        }
    })
}
export const addTzOrganizationLife = (data) => {
    return request('/addTzOrganizationLife', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'addTzOrganizationLife',
        bizParams: {
            data: data
        }
    })
}

export const getInfo = (data) => {
    return request('/getInfo', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'getInfo',
        bizParams: {
            ...data
        }
    })
}
export const queryMeetingClass = (data) => {
    return request('/queryMeetingClass', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'queryMeetingClass',
        bizParams: {
            ...data
        }
    })
}
export const queryMeetingClassType = (data) => {
    return request('/queryMeetingClassType', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'queryMeetingClassType',
        bizParams: {
            ...data
        }
    })
}

export const findImgById = (id) => {
    return request('/findImgById', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'findImgById',
        bizParams: {
            id: id
        }
    })
}
export const cancelMeeting = (data) => {
    return request('/cancelMeeting', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'cancelMeeting',
        bizParams: {
            data: data
        }
    })
}
export const startMeeting = (id) => {
    return request('/startMeeting', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'startMeeting',
        bizParams: {
            id: id
        }
    })
}
export const deleteTzOrganizationLife = (ids) => {
    return request('/deleteTzOrganizationLife', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'deleteTzOrganizationLife',
        bizParams: {
            ids: ids
        }
    })
}

export const queryTzOrganizationLifeList = (data) => {
    return request('/queryTzOrganizationLifeList', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'queryTzOrganizationLifeList',
        bizParams: {
            ...data
        }
    })
}
export const deleteFileById = (id) => {
    return request('/deleteFileById', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'deleteFileById',
        bizParams: {
            id: id
        }
    })
}

export const saveStudyFile = (data) => {
    return request('/saveStudyFile', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'saveStudyFile',
        bizParams: {
            ...data
        }
    })
}
export const querytaizhang = (data) => {
    return request('/querytaizhang', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'querytaizhang',
        bizParams: {
            ...data
        }
    })
}
export const saveImg = (data) => {
    return request('/saveImg', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'saveImg',
        bizParams: {
            data:data
        }
    })
}

export const findStudyFile = (id) => {
    return request('/findStudyFile', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'findStudyFile',
        bizParams: {
            meetingId: id
        }
    })
}
export const countMyTotalClassNum = (id) => {
    return request('/countMyTotalClassNum', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'countMyTotalClassNum',
        bizParams: {
             id:id+''
        }
    })
}

export const setPlanMeetingParty = (params) => {
    return request('/setPlanMeetingParty', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'setPlanMeetingParty',
        bizParams: {
            ...params
        }
    })
}

export const getOrganizationLifeById = (id) => {
    return request('/getOrganizationLifeById', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'getOrganizationLifeById',
        bizParams: {
            id: id+''
        }
    })
}

export const getPlanMeetingPartyMemberInfo = (id) => {
    return request('/getPlanMeetingPartyMemberInfo', {
        serviceName: 'TzOrganizationLifeService',
        methodName: 'getPlanMeetingPartyMemberInfo',
        bizParams: {
            id: id+''
        }
    })
}
