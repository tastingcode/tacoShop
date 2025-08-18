import http from 'k6/http';
import {sleep} from 'k6';


const BASE_URL = 'http://localhost:8080';

export const options = {
    stages: [
        {duration: '4m', target: 30000},
    ],
}

export default function () {

    http.get(`${BASE_URL}/api/v1/products?brandId=95&productSortType=LIKES_DESC&page=0&size=10`);

    sleep(1)
}
