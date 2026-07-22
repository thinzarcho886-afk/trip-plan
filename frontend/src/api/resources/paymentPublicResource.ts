import { ApiResources } from '../index.js';

// define api resource name
type PaymentPublicApiResource =
    | 'getPaymentMethods'
    | 'getById'
    ;


export const paymentPublicApiResource: ApiResources<PaymentPublicApiResource> = {
    getPaymentMethods: { method: 'get', url: '/payment-method' },

    getById: { method: 'get', url: '/payment-method/:id' },


};
