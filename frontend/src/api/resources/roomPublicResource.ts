/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';

// define api resource name
type RoomPublicApiResource =
  | 'getRooms'
  | 'getById'

export const roomPublicApiResource: ApiResources<RoomPublicApiResource> = {
  getRooms:{method: 'get' , url:'/room'},
  getById: { method: 'get', url: '/room/:id' },
};
