import { ApiResources } from '../index.js';

// define api resource name
type CustomerApiResource =
  | 'getCustomers'
  | 'register'
  | 'update'
  | 'getById'
  | 'saveCustomer'
  | 'getByCustomerId'
  | 'updateCustomer'
  | 'getByUserId';


export const customerApiResource: ApiResources<CustomerApiResource> = {
  getCustomers: { method: 'get', url: '/auth/customer' },
  register: { method: 'post', url: '/auth/customer' },
  update: { method: 'put', url: '/auth/customer' },
  getById: { method: 'get', url: '/auth/customer/:id' },
  getByUserId: { method: 'get', url: '/auth/customer/:id' },

  getByCustomerId: { method: 'get', url: '/customer/:id' },
  updateCustomer: { method: 'put', url: '/customer' },
  saveCustomer: { method: 'post', url: '/customer' },
};
