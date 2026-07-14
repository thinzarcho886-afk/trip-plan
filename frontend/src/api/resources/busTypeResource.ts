import { ApiResources } from '..';

// define api resource name
type BusTypeApiResource =
  | 'getBusTypes'
  | 'register'
  | 'save'
  | 'update'
  | 'getById'
//   |'getByName'
//   | 'getByStatus';

const baseUrl = '/auth/bus-type';

export const busTypeApiResource: ApiResources<BusTypeApiResource> = {
  getBusTypes: {method: 'get', url: baseUrl},
  register: { method: 'post', url: baseUrl },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
//   getByName:{ method: 'get', url: baseUrl + '/by-name/:name' },
//   getByStatus: { method: 'get', url: baseUrl + '/by-status/:status' },
};
