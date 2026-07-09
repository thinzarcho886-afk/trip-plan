import { ApiResources } from '..';

// define api resource name
type RoomApiResource =
  | 'getRooms'
  | 'register'
  | 'update'
  | 'save'
  | 'getById'
  | 'getByRoomNo'
  | 'getByRoomTypeName'
  | 'getByStatus';

const baseUrl = '/auth/room';

export const roomApiResource: ApiResources<RoomApiResource> = {
  getRooms: { method: 'get', url: baseUrl },
  register: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
  save: {method: 'post', url: baseUrl},
  getById: { method: 'get', url: baseUrl + '/:id' },
  getByRoomNo: { method: 'get', url: baseUrl + '/by-room-no/:roomNo' },
  getByRoomTypeName: { method: 'get', url: baseUrl + '/by-room-type-name/:roomTypeName' },
  getByStatus: { method: 'get', url: baseUrl + '/by-status/:status' },
};
