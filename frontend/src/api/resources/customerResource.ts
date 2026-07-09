import { ApiResources } from '../index.js';

type CustomerApiResource = 'getList' | 'getById' | 'save' | 'update';

const baseUrl = '/auth/customer';

export const customerApiResource: ApiResources<CustomerApiResource> = {
  getList: { method: 'get', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
};
