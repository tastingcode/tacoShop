import http from 'k6/http';
import { sleep } from 'k6';

const BASE_URL = 'http://localhost:8080'; // API 서버 주소

export const options = {
    stages: [
        {duration: '1m', target: 1000},
    ],
}

export default function () {
    const payload = JSON.stringify({
        orderId: 1,
        paymentType: 'CREDIT_CARD',
        cardType: 'SAMSUNG',
        cardNo: '1234-5678-9814-1451',
        amount: 5000,
        callbackUrl: 'http://localhost:8080/api/v1/payments/callback',
    });

    const headers = {
        'Content-Type': 'application/json',
        'X-USER-ID': 'testUser',
    };

    http.post(`${BASE_URL}/api/v1/payments`, payload, { headers });

    sleep(1);
}
