/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';

// define api resource name
type RoomTypePublicApiResource =
  | 'getRoomTypes'
  | 'getById'

export const roomTypePublicApiResource: ApiResources<RoomTypePublicApiResource> = {
  getRoomTypes:{method: 'get' , url:'/room_type'},
  getById: { method: 'get', url: '/room_type/:id' },
};
