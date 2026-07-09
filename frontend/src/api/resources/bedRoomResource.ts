import { ApiResources } from '..';

type BedRoomApiResource = 'getList' | 'getById' | 'save' | 'update';

const baseUrl = '/auth/bed-room';

export const bedRoomApiResource: ApiResources<BedRoomApiResource> = {
  getList: { method: 'get', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },
  save: { method: 'post', url: baseUrl },
  update: { method: 'put', url: baseUrl },
};
