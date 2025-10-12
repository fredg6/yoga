import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { of } from 'rxjs';
import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpClient: HttpClient;

  const id = '1';
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientModule
      ]
    });
    service = TestBed.inject(UserService);
    httpClient = TestBed.inject(HttpClient);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  
  it('should call httpClient.get() and return an observable that emits the requested user on getById', () => {
    const mockedUser = {
      id: 1,
      email: "yoga@studio.com",
      lastName: "Admin",
      firstName: "Admin",
      admin: true,
      password: "",
      createdAt: Date.now(),
      updatedAt: Date.now()
    };
    const spiedHttpClientGet = jest.spyOn(httpClient, 'get').mockReturnValue(of(mockedUser));

    service.getById(id).subscribe(value => {
      expect(spiedHttpClientGet).toHaveBeenCalledWith(`${service['pathService']}/${id}`);
      expect(value).toEqual(mockedUser);
    });
  });
  
  it('should call httpClient.delete() and return an observable on delete', () => {
    const spiedHttpClientDelete = jest.spyOn(httpClient, 'delete').mockReturnValue(of());

    service.delete(id).subscribe(() =>
      expect(spiedHttpClientDelete).toHaveBeenCalledWith(`${service['pathService']}/${id}`)
    );
  });
});
