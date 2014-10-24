def login_user
  visit 'http://localhost:8080'
  fill_in('Username', :with => 'admin')
  fill_in('Password', :with => 'admin')
  click_button('Sign in')
end