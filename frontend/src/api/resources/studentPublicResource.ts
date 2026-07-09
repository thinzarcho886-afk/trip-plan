/**
 * User API Resource
 * working example of user api with typescript
 */
import { ApiResources } from '../index.js';

// define api resource name
type StudentPublicApiResource =
 
  | 'getAllStudents'
  | 'getStudentById'
  | 'getStudentByName'
  | 'getStudentByEmail';

 
export const studentPublicApiResource: ApiResources<StudentPublicApiResource> = {
  
  getAllStudents:{ method: 'get', url: '/student' },
  getStudentByName:{ method: 'get', url: '/student/:name' },
  getStudentByEmail:{ method: 'get', url: '/student/:studentName' },


  getStudentById: { method: 'get', url: '/student/:id' },
  
}
