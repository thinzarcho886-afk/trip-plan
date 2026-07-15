import { ApiResources } from '../index.js';

// define api resource name
type PaymentMethodApiResource =
  | 'getPaymentMethods'
  | 'register'
  | 'update'
  | 'getById'
  | 'getByStatus';


export const paymentMethodApiResource: ApiResources<PaymentMethodApiResource> = {
  getPaymentMethods: { method: 'get', url: '/auth/payment-method'},
  register: { method: 'post', url: '/auth/payment-method' },
  update: { method: 'put', url: '/auth/payment-method' },
  getById: { method: 'get', url: '/auth/payment-method/:id' },
  getByStatus: { method: 'get', url: '/auth/payment-method/:status' },

};
