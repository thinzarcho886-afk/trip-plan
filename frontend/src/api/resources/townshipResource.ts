import { ApiResources } from '..';

// define api resource name
type TownshipApiResource =
  | 'getList'
  | 'save'
  | 'update'
  | 'getById'
  |'getBytownshipName'
  | 'getByStatus';

const baseUrl = '/auth/township';

export const townshipApiResource: ApiResources<TownshipApiResource> = {
  getList: { method: 'get', url: baseUrl },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  getBytownshipName:{ method: 'get', url: baseUrl + '/by-name/:townshipName' },
  getByStatus: { method: 'get', url: baseUrl + '/by-status/:status' },
};
