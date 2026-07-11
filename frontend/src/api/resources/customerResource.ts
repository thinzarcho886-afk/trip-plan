import { ApiResources } from '../index.js';

// define api resource name
type CustomerApiResource =
  | 'getCustomers'
  | 'register'
  | 'update'
  | 'getById';

const baseUrl = 'customer';

export const customerApiResource: ApiResources<CustomerApiResource> = {
  getCustomers: { method: 'get', url: baseUrl },
  register: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
};
