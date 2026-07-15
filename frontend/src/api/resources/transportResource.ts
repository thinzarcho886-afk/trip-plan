import { ApiResources } from '..';

// define api resource name
type TransportApiResource =
  | 'getList'
  | 'register'
  | 'save'
  | 'update'
  | 'getById'
//   |'getByName'
//   | 'getByStatus';

const baseUrl = '/auth/transport';

export const transportApiResource: ApiResources<TransportApiResource> = {
  getList: {method: 'get', url: baseUrl},
  register: { method: 'post', url: baseUrl },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
//   getByName:{ method: 'get', url: baseUrl + '/by-name/:name' },
//   getByStatus: { method: 'get', url: baseUrl + '/by-status/:status' },
};
