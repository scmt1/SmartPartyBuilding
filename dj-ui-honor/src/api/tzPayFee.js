import request from '../utils/request';

// 根据年份和部门id统计支部缴纳党费
export const queryFeeTotalByYear = (params) => {
    return request('/queryFeeTotalByYear', {
        serviceName: 'TzPayFeeService',
        methodName: 'queryFeeTotalByYear',
        bizParams: {
            ...params,
        },
    });
};
// 根据月份和部门id统计支部缴纳党费
export const queryFeeTotalByMonth = (params) => {
    return request('/queryFeeTotalByMonth', {
        serviceName: 'TzPayFeeService',
        methodName: 'queryFeeTotalByMonth',
        bizParams: {
            ...params,
        },
    });
};
