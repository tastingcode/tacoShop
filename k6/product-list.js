import http from 'k6/http';
import {sleep} from 'k6';


const BASE_URL = 'http://localhost:8080';

export const options = {
    stages: [
        { duration: '1m', target: 30},
        { duration: '2m', target: 50 },
        { duration: '3m', target: 100 },
        { duration: '1m', target: 0 },
    ],
}

export default function () {

    http.get(`${BASE_URL}/api/v1/products?brandId=95&productSortType=LIKES_DESC&page=0&size=15`);

    sleep(1)
}
