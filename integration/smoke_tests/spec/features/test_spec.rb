require 'spec_helper'
require 'step_definitions/common_steps'

feature "Logging in" do

  background do
    login_user_bill
  end

  scenario "Signing in takes you to the 'You' timeline" do
    expect(page).to have_selector('.sprite-user')
  end

end