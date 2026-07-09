import { ApiResources } from '..';

// define api resource name
type RoomTypeApiResource =
    'getAll'
  | 'getList'
  | 'save'
  | 'update'
  | 'getById'
  |'getByName'
  | 'getByStatus';

const baseUrl = '/auth/room_type';

export const roomTypeApiResource: ApiResources<RoomTypeApiResource> = {
  getAll: {method: 'get', url: baseUrl},
  getList: { method: 'get', url: baseUrl },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  getByName:{ method: 'get', url: baseUrl + '/by-name/:name' },
  getByStatus: { method: 'get', url: baseUrl + '/by-status/:status' },
};
