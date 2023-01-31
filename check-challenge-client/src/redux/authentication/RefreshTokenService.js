import axios from "axios";

export async function refreshTokenIfExpired () {
    let isExpire = checkIfTokenIsExpired();

    if (isExpire) {
            let sessionData = JSON.parse(localStorage.getItem('userSession'));
            let refreshToken = sessionData.refreshToken;
            let email = sessionData.user.sub;

            var dataRequest = {
                email: email,
                refreshToken: refreshToken
            }
            await axios.post("auth/refresh", dataRequest)
            .then(async response => {
                var newToken = response.data.access_token;
                var sessionData = JSON.parse(localStorage.getItem("userSession"));
                sessionData.accessToken = await newToken;
                localStorage.setItem("userSession", JSON.stringify(sessionData));
            })
            .catch(async error => {
                return Promise.reject(await error);
            });
    }
};

export const checkIfTokenIsExpired = () => {
    let sessionData = JSON.parse(localStorage.getItem('userSession'));
    let expireAt = sessionData.user.exp;
    return /* Date.now() >= expireAt * 1000; */ true;
};
