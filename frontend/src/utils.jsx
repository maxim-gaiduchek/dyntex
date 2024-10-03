import axios from 'axios';
import BaseUrl from './BaseUrl';

export const getUser = async (token) => {
    if (token === undefined) {
        return false
    }

    try {
        const response = await axios.get(BaseUrl + "/api/users/authenticated", {
            headers: {
                'Authorization': token
            }
        });
        return response.data;
    } catch (e) {
        return false
        // console.error(e);
        // removeCookie("token");
        // navigate("/login");
    }
};