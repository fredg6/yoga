/// <reference types="cypress" />

describe('Not found spec', () => {
  it('should display not-found page', () => {
    cy.visit('/page-inexistante');

    cy.url().should('include', '/404');
    cy.get('[data-testid="notFound"]').should('have.text', 'Page not found !');
  });
});