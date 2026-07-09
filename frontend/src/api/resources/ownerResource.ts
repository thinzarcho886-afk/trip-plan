/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';

// define api resource name
type OwnerApiResource =
  | 'createOwner'
  | 'updateOwner'
  |'getAllOwners'
  | 'getOwnerById'
  | 'getOwnerByName'
  |'getOwnerByPhone'
  |'getOwnerByAddress'

export const ownerApiResource: ApiResources<OwnerApiResource> = {
  createOwner: { method: 'post', url: '/auth/owner' },
  updateOwner: { method: 'put', url: '/auth/owner' },
  getAllOwners:{ method: 'get', url: '/auth/owner/by-list' },
  getOwnerById: { method: 'get', url: '/auth/owner/:id' },
  getOwnerByName: { method: 'get', url: '/auth/owner/search-by-name' },
  getOwnerByPhone: { method: 'get', url: '/auth/owner/search-by-phone' },
  getOwnerByAddress: { method: 'get', url: '/auth/owner/search-by-address' },
};
