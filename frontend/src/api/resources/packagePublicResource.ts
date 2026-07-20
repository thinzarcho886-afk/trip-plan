import { ApiResources } from '..';

// define api resource name
type PackagePublicApiResource =
  | 'getPackages'
 ;
const baseUrl = '/package';

export const packagePublicApiResource: ApiResources<PackagePublicApiResource> = {
  getPackages: {method: 'get', url: baseUrl},
 
};
