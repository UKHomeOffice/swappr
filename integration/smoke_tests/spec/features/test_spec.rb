require 'spec_helper'
require 'step_definitions/common_steps'

feature "Logging in" do

  background do
    login_user_bill
  end

  scenario "Signing in takes you to the 'You' timeline" do
    expect(page).to have_selector('.sprite-user')
  end

  scenario "You can add a shift" do
    add_shift(1, 'CFH - Mid')
    expect(page.all('.t-day')[1]).to have_selector('.t-swap-shift')
  end

  scenario "You can offer to swap a shift" do
    offer_shift(3, 'BFH - Early', 'S1H - Late')
    expect(page.all('.t-day')[3].find('.t-status')).to have_content('Swap requested')
  end

  scenario "You can volunteer to swap for an offered shift" do
    date = page.all('.t-day')[5]['data-t-date']
    offer_shift(5, 'CFH - Mid', 'C1H - Mid')
    logout
    login_user_ben
    find('.t-nav-timeline').click
    find_day_row_by_date(date).find('.t-button-volunteer').click
    expect(find_day_row_by_date(date).find('.t-status')).to have_content("You've volunteered to swap")
  end

end