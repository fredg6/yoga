import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { UserService } from 'src/app/services/user.service';
import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;

  const mockedSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  };
  const now = new Date();
  const mockedUser = {
    id: 1,
    email: "yoga@studio.com",
    lastName: "Admin",
    firstName: "Admin",
    admin: true,
    password: "",
    createdAt: now,
    updatedAt: now
  };
  const mockedMatSnackBar = { open: jest.fn() };
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockedSessionService },
        { provide: MatSnackBar, useValue: mockedMatSnackBar },
        DatePipe
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;

    userService = TestBed.inject(UserService);
    jest.spyOn(userService, 'getById').mockReturnValue(of(mockedUser));
    jest.spyOn(userService, 'delete').mockReturnValue(of(undefined));
    
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set user on init', () => {
    component.ngOnInit();

    expect(userService.getById).toHaveBeenCalledWith(mockedSessionService.sessionInformation.id.toString());
    expect(component.user).toBe(mockedUser);
  });

  it('should correctly display admin user account on init', () => {
    const datePipe = TestBed.inject(DatePipe);
    
    component.ngOnInit();

    let pElements = fixture.nativeElement.querySelectorAll("p");
    expect(pElements).toHaveLength(5);
    expect(pElements[0].textContent).toContain(`Name: ${mockedUser.firstName} ${mockedUser.lastName.toUpperCase()}`);
    expect(pElements[1].textContent).toContain(`Email: ${mockedUser.email}`);
    expect(pElements[2].textContent).toContain('You are admin');
    expect(pElements[3].textContent).toContain(`Create at:  ${datePipe.transform(mockedUser.createdAt, 'longDate')}`);
    expect(pElements[4].textContent).toContain(`Last update:  ${datePipe.transform(mockedUser.updatedAt, 'longDate')}`);
  });

  it('should display delete button for not admin user on init', () => {
    mockedUser.admin = false;
    fixture.detectChanges();
    
    component.ngOnInit();

    let deleteButton = fixture.nativeElement.querySelector('button[color="warn"]');
    expect(deleteButton).toBeDefined();
  });

  it('should delete connected user, inform and logout him/her', () => {
    const router = TestBed.inject(Router);
    const spiedNavigate = jest.spyOn(router, 'navigate');
    
    component.delete();
    
    expect(userService.delete).toHaveBeenCalledWith(mockedSessionService.sessionInformation.id.toString());
    expect(mockedMatSnackBar.open).toHaveBeenCalledWith("Your account has been deleted !", 'Close', { duration: 3000 });
    expect(mockedSessionService.logOut).toHaveBeenCalled();
    expect(spiedNavigate).toHaveBeenCalledWith(['/']);
  });
});
