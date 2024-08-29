// const baseURL = 'http://101.206.141.236:7380/fastcms'
// const baseURL = 'http://10.4.117.31:7380/fastcms'
const baseURL = '/fastcmsApi'//新版
// const baseURL = 'http://192.168.6.6:7380/fastcms'
export const request = (url, options) => {
    const headers = {
        'Content-Type': 'application/json',
        Accept: 'application/json',
    };

    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', baseURL + url);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.setRequestHeader('Accept', 'application/json');

        xhr.onload = () => {
            if (xhr.status === 200) {
                const data = JSON.parse(xhr.responseText);
                const code = data.code;

                if (code !== 200) {
                    return;
                }

                resolve(data);
            } else {
                reject(xhr.statusText);
            }
        };

        xhr.onerror = () => {
            reject(xhr.statusText);
        };

        xhr.send();
    });
};
