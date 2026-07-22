import { ApiResources } from '..';

// define api resource name
type PackagePublicApiResource =
  | 'getPackages'
  | 'getById';
const baseUrl = '/package';

export const packagePublicApiResource: ApiResources<PackagePublicApiResource> = {
  getPackages: { method: 'get', url: baseUrl },
  getById: { method: 'get', url: baseUrl + '/:id' },


};
