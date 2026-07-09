import { ApiResources } from '..';

// define api resource name
type StreetApiResource =
    'getAll'
  | 'getList'
  | 'save'
  | 'update'
  | 'getById'
  |'getByStreetName'
  | 'getByStatus';

const baseUrl = '/auth/street';

export const streetApiResource: ApiResources<StreetApiResource> = {
  getAll: {method: 'get' , url:baseUrl},
  getList: { method: 'get', url: baseUrl },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  getByStreetName:{ method: 'get', url: baseUrl + '/by-name/streetName' },
  getByStatus: { method: 'get', url: baseUrl + '/by-status/status' },
};
